pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //maven { setUrl("https://devrepo.kakaomobility.com/repository/kakao-mobility-android-knsdk-public/")}
        //maven { setUrl("https://www.jitpack.io")}
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven {
            setUrl("https://repository.map.naver.com/archive/maven")
        }
    }
}

rootProject.name = "Traffic_Light_with_Arduino"
include(":app")
 