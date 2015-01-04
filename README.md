Reminder2
=========
Made to a friend's requirement.

This is an app with two function : Add Reminder and Remove Reminder.

UI : 
contains two fragments. The Add page includes a time picker and an edit Text. The remove page includes a remove button.

MainActivity :  
has two implements from Section1(Add) and Section2(remove). Initialation and removement of AlarmManager is at here.
Also, The alertDialog shows at here.

=========
Bug:

After Closing AlertDialog with finish(), it is still at MainActivity, can't onPause or onDestroy the Application.
Might shows Not Responce sometimes.
