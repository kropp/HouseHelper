import Shared
import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    AppKt.navigateToDeepLink(uri: url.absoluteString)
                }
        }
    }
}
