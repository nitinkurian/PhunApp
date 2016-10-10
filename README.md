PhunApp
========

Quick Gist
========
This is Phunware's Sample app to test out Android development skills.
* Display's the Mission data in a Master/Detail type style
* Master Activity displays all the Missions
* Detail Activity displays detail about a mission
* Schedule times are displayed in the timezone of the device
* A mission event can be shared via multiple channels in both the screens
* The app runs in all orientations
* Works on API 16 and above
* Adopted AppCompat Library for Material Design backwards compatibility.
* Is not prone to crashes
* App supports orientation changes.  

Time Taken
-------
12 hours

Significat time was taken to implement the circular imageview/image for each item in Grid View. Ended up implementing that by creating a rounded bitmap drawable for every image downloaded. 
Ref: http://stackoverflow.com/questions/16208365/how-to-create-a-circular-imageview-in-android

Extras
=======
A partial solution for Collapsing Header transition is done. 
https://developer.android.com/reference/android/support/design/widget/CollapsingToolbarLayout.html was used for this. 


Additional Notes
-------
* AsyncTask is used for loading images and the startup data in the background.
* Once downloaded, the image bitmaps are marshalled (parcelled) to account for a combination of orinetation change and network loss.
* External libaries were not used. In an app of  bigger scale, I would switch image loading and caching to external libraries such as Picaso instead of directly using AsyncTask due to performance reasons and/or disk/memory caching capability instead of reiventing the wheel.
* It works offline, once the initial data is downloaded. 
* The detail page image won't load if the device is offline as the image is refetched. *This is a candidate for optimization*
* Although scrolling performance seems decent, no performance analysis has been done. There are optimization possible by flattening some of the layouts.
* Althought Unit test package is present, no unit test implemenattion is present.
* Circular bitmap is cached/marshalled locally for the activity lifecycle.

This has been tested only on 2 Genymotion Emulators (API 17 Nexus 10 and API 22 Nexus 6) since the only test Android device I have doesnt seem  to be working. 
