import ComposeApp
import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    AppKt.navigateToDeepLink(uri: url.absoluteString)
                }
        }
    }
}
