// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    // 下記のように直接指定も可能だが、
    // 「application」や「compose」と合わせて、「libs.version.toml」での指定を参照(一括バージョン管理)
//    kotlin("plugin.serialization") version "2.2.10" apply false
}