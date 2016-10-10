PhunApp
========

Quick Gist
==
This is Phunware's Sample app to test out Android development skills.
* Display's the Mission data in a Master/Detail type style
* Master Activity displays all the Missions
* Detail Activity displays detail about a mission
* Schedule times are displayed in the timezone of the device
* A mission event can be shared via multiple channels in both the screens
* The app runs in all orientations
* Works on API 16 and above
* Adopted AppCompat Library for Material Design backwards compatibility.
* App supports orientation changes.  
* AsyncTask is used for loading images and the startup data in the background.
* The circular image bitmaps are marshaled (parceled) during activity lifecycle to account for a combination of orientation change and network loss.

Time Taken
==
12 hours

A major portion of time was taken to implement the circular ImageView/image for each item in Grid View. Ended up implementing that by creating a rounded bitmap drawable  every image downloaded.
Ref: http://stackoverflow.com/questions/16208365/how-to-create-a-circular-imageview-in-android

Extras
==
A partial solution for Collapsing Header transition is done.
https://developer.android.com/reference/android/support/design/widget/CollapsingToolbarLayout.html was used for this.


Improvements/To do's
==
* External libraries were not used. In an app of  bigger scale, I would switch image loading and caching to external libraries such as Picaso instead of directly using AsyncTask due to performance reasons and/or disk/memory caching capability instead of reinventing the wheel.- *Optimization*
* It works offline, once the initial data is downloaded. Needs more work to make this work offline on launch. - *Feature Improvement*
* The detail page image won't load if the device is offline as the image is refetched. - *Optimization*
* Although scrolling performance seems decent, no performance analysis has been done. There are optimization possible by flattening some of the layouts. - *Optimization*
* Although Unit test package is present, no unit test implementation is done. *Code Improvement*
* Image Load Async Task could be reused across the two activities. Its not done here. There are separate AsyncTask for this purpose in each activity. - *Code Improvement*

Test Setup
==
This has been tested only on 2 Genymotion Emulators (API 17 Nexus 10 and API 22 Nexus 6) since the only test Android device I have doesnt seem  to be working.
