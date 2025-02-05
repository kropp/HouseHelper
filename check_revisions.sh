#!/bin/bash

# Script to check out each git revision from starter to solutions branch
# and run the application for 10 seconds

set -e  # Exit immediately if a command exits with a non-zero status

# Find the latest commit in the current branch with message containing "Run starter project"
STARTER_COMMIT=$(git log --grep="Run starter project" --format="%H" -n 1)
if [ -z "$STARTER_COMMIT" ]; then
    echo "Error: Could not find commit with message 'Run starter project' in the current branch"
    exit 1
fi

# Get all commits from the starter commit to solutions
COMMITS=$(git log --reverse --format="%H" $STARTER_COMMIT..solutions)
COMMITS="$STARTER_COMMIT $COMMITS"  # Include the starter commit

# Function to run the application and stop it after 10 seconds
run_app_with_timeout() {
    echo "Running application..."

    # Run the application in the background and capture its PID
    ./gradlew desktopRun -DmainClass=com.kotlinconf.workshop.househelper.MainKt --quiet &
    APP_PID=$!

    # Flag to track if the application started successfully
    APP_STARTED=false

    # Wait for the build to complete or for the app to start running
    # We'll check every second for up to 60 seconds
    for i in {1..60}; do
        # Check if the app is running (by looking for a specific process)
        if ps -p $APP_PID | grep -q java; then
            echo "Application is running, waiting for 10 seconds..."
            APP_STARTED=true

            # Wait for 10 seconds, checking every second if the process is still running
            for j in {1..10}; do
                # Check if the process is still running
                if ! ps -p $APP_PID > /dev/null; then
                    echo "Application terminated before the 10 second timeout!"
                    # If it terminated with an error, exit the script
                    wait $APP_PID
                    EXIT_CODE=$?
                    if [ $EXIT_CODE -ne 0 ]; then
                        echo "Application terminated with error code: $EXIT_CODE"
                        return 1
                    fi
                    break 2
                fi

                # Wait for 1 second before checking again
                sleep 1
            done

            # Kill the application after 10 seconds if it's still running
            if ps -p $APP_PID > /dev/null; then
                echo "Stopping application after 10 seconds..."
                kill $APP_PID
            fi
            break
        fi

        # Wait a bit before checking again
        sleep 1
    done

    # Check if the application never started (build failure or other issue)
    if [ "$APP_STARTED" = false ]; then
        echo "Application failed to start within the timeout period!"
        return 1
    fi

    # Kill the application
    if ps -p $APP_PID > /dev/null; then
        echo "Stopping application..."
        kill $APP_PID
    else
        echo "Application already stopped"
    fi

    # Wait for the process to fully terminate
    wait $APP_PID 2>/dev/null || true

    # No need to check the final build status as we're using the APP_STARTED flag

    return 0
}

# Iterate through each commit
for COMMIT in $COMMITS; do
    COMMIT_SHORT=$(git rev-parse --short $COMMIT)
    COMMIT_MSG=$(git log -1 --format="%s" $COMMIT)

    echo "========================================================"
    echo "Checking out commit: $COMMIT_SHORT - $COMMIT_MSG"
    echo "========================================================"

    # Check out the commit (quiet mode)
    git checkout -q $COMMIT

    # Run the application and handle potential build failures
    if ! run_app_with_timeout; then
        echo "ERROR: Build failed for commit $COMMIT_SHORT - $COMMIT_MSG"
        exit 1
    fi

    echo "Successfully ran application for commit: $COMMIT_SHORT"
    echo ""
done

echo "All revisions checked successfully!"

# Check out the solutions branch (quiet mode)
git checkout -q solutions
echo "solutions branch is now checked out."
