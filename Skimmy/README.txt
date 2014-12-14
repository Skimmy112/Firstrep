README
-----------------
SKIMMY: THE VISION
-----------------
Skimmy’s original function was to accept plain text or URLs, analyze the text from that source, and return to the user a  “skimmed” version of their original input. The skimmed version would be a summary/outline that reworded the original text into a shorter, simplified version.
 
Unfortunately, with one group member who dropped the class and another that failed to attend meetings and match the contributions of the rest of the group, our power duo ended up focusing on the core functions of the app. We look forward to developing additional features including:
	- Extract text from other sites
	- Evernote integration
	- A word ranking system (to help identify keywords)
	- Abstraction based summarization (currently extraction based)
	- Desktop and iOS versions
	- Much more!
-----------------
FUNCTIONS
-----------------
Skimmy accepts user inputted URLs  (Wikipedia or .txt) or text and a user selected keyword and upon tapping the “Skim Me!” button, returns to the user all of the sentences within the text source that contain that specific keyword. Result can be copied to the clipboard by simply tapping on the result until the box turns grey.
-----------------
PURPOSE
-----------------
This app would be a staple for those who have to complete high volumes of reading to pinpoint a main idea or theme in a limited amount of time. It highlights the “keyword” and focuses on creating an outline containing that keyword or phrase. This app would also be a great research aid, allowing research materials to be skimmed through easily in order to find more specific information. Many students would benefit heavily from this app.
-----------------
DIFFICULTIES
-----------------
We faced some difficulties dealing with connecting to the network. We ultimately settled on Async to handle the task but Async is complex and required quite a bit of tinkering and research to get it to work properly. 

The webReader, which came with the Async tutorial, also presented some issues. It was limited to a fixed number of characters which we managed to bypass using bufferedReader. Initially, using bufferedReader created a large delay in processing time which we were able to trim down significantly by rewriting the method.

In an attempt to employ the Wikipedia API, we realized it would be simpler to write our own, which turned out to be more effective.
