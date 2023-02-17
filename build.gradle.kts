import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
//	id("com.github.johnrengelman.shadow") version "7.1.2"
	id("io.gitlab.arturbosch.detekt").version("1.22.0")
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"

}

group = "dev.sihan"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")

	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.graphql:spring-graphql-test")
}

//tasks.withType<ShadowJar> {
//	archiveBaseName.set("pos")
//	archiveClassifier.set("all")
//	manifest {
//		attributes["Main-Class"] = "dev.sihan.pos.PosApplicationKt"
//	}
//}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Detekt>().configureEach {
	jvmTarget = "17"
}

detekt {
	buildUponDefaultConfig = true
	allRules = false
	autoCorrect = true
	config = files("$projectDir/config/detekt.yml")
}

//tasks.withType<Detekt>().configureEach {
//	reports {
//		html.required.set(true)
//		md.required.set(true)
//	}
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
