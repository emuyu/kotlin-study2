import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
	id("org.springframework.boot") version "2.7.10"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("io.gitlab.arturbosch.detekt") version "1.21.0"
	id("org.jetbrains.dokka") version "1.7.20"
	id("org.openapi.generator") version "6.2.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.arrow-kt:arrow-core:1.1.3")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.postgresql:postgresql")
	implementation("com.github.database-rider:rider-core:1.35.0")
	implementation("com.github.database-rider:rider-spring:1.35.0")
	testImplementation("com.github.database-rider:rider-junit5:1.35.0")
	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
	dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.7.20")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.assertj:assertj-core:3.23.1")
	testImplementation("net.jqwik:jqwik:1.7.1")
	testImplementation("net.jqwik:jqwik-kotlin:1.7.1")
	compileOnly("io.swagger.core.v3:swagger-annotations:2.2.6")
	compileOnly("io.swagger.core.v3:swagger-models:2.2.6")
	compileOnly("jakarta.annotation:jakarta.annotation-api:2.1.1")

	implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

/**
 * detektの設定
 *
 * 基本的に全て `detekt-override.yml` で設定する
 */
detekt {
	/**
	 * ./gradlew detektGenerateConfig でdetekt.ymlが生成される(バージョンが上がる度に再生成する)
	 */
	config = files(
		"$projectDir/config/detekt/detekt.yml",
		"$projectDir/config/detekt/detekt-override.yml",
	)
}

/**
 * OpenAPI Generator を使ってコード生成
 */
task<GenerateTask>("generateApiServer") {
	generatorName.set("kotlin-spring")
	inputSpec.set("$projectDir/docs/openapi.yaml")
	outputDir.set("$buildDir/openapi/server-code/") // .gitignoreされているので注意(わざとここにあります)
	apiPackage.set("com.example.implementingserversidekotlindevelopment.openapi.generated.controller")
	modelPackage.set("com.example.implementingserversidekotlindevelopment.openapi.generated.model")
	configOptions.set(
		mapOf(
			"interfaceOnly" to "true",
		)
	)
	/**
	 * true にすると tags 準拠で、API の interface を生成する
	 */
	additionalProperties.set(
		mapOf(
			"useTags" to "true"
		)
	)
}

/**
 * Kotlinをコンパイルする前に、generateApiServerタスクを実行
 * 必ずスキーマファイルから最新のコードが生成され、もし変更があったらコンパイル時に失敗して気付けるため
 */
tasks.compileKotlin {
	dependsOn("generateApiServer")
}

/**
 * OpenAPI Generator によって生成されたコードを import できるようにする
 */
kotlin.sourceSets.main {
	kotlin.srcDir("$buildDir/openapi/server-code/src/main")
}
