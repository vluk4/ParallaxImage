# ParallaxImage
ParallaxImage is a Jetpack Compose library that provides a ParallaxImage composable function to load and display images with a parallax effect based on the device's sensors. It utilizes the Coil library for image loading and is compatible with Android 12 (API level 31) and above.

<img src="https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExZGM1ZWZjNWVlN2JkODY1MmQ3ODYyNDgwZjdiMTlhYTgwOWQ1ZWMwNyZjdD1n/RlZemRZAurcZ29lCEN/giphy.gif" width="264" height="480" />

### Features
* Load and display images using the Coil library
* Create a parallax effect based on the device's sensors
* Compatible with Android 12 and above
### Installation
To add ParallaxImage to your project, add the following dependency to your app's **build.gradle** file:
```
dependencies {
    implementation("com.github.vluk4:parallax_view:1.0.3")
}
```
And jitpack on repositories:
```
repositories {
    maven { url = uri("https://jitpack.io") }
}
```
### Usage
Import the ParallaxImage composable function in your code:
```
import com.vluk4.parallaximage.ParallaxImage
```
Use the **ParallaxImage** composable in your UI:
```
ParallaxImage(
    model = "https://path.to/your/image.jpg",
    modifier = Modifier.fillMaxSize(),
    depthMultiplier = 20,
    shape = CircleShape,
    contentScale = ContentScale.Crop,
    contentDescription = "A sample parallax image"
)
```
### Parameters
* **model**: Any object representing the image. It can be a URL, a resource ID, or any other object supported by the Coil library.
* **modifier**: (Optional) Modifier to apply to the image.
* **depthMultiplier**: (Optional) A factor that determines the depth of the parallax effect.
* **shape**: (Optional) The shape to be applied to the image.
* **contentScale**: (Optional) How the image should be scaled.
* **contentDescription**: (Optional) A description for the image.
