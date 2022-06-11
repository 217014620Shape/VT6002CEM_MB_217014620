# VT6002CEM_MB_217014620

Student Name: Man Chun Ngai
Student ID: 217014620
Coursework Title : Designing and Building a mobile App (CW2)

Introduction
MainActivty
| (Data: username - store in sharedPreferences)
| (Layout: Activity_main.xml)
| (Function: BiometricPrompt - for auth fingerprint)
|
ShopListActivity
| (Data: all shop information in text - store in firebase realtimeDatabase)
| (Data: shop image - store in firebase storage)
| (Layout: home_page_layout.xml)
| (Adapter layout: home_page_item_list.xml)
| (Function: Map - for showing all shops locaiton from list in the map & current location would used)
| (Function: Map - layout: maps_page_layout)
|
DetailListActivity
| (Data: all shop information in text - store in firebase realtime database)
| (Data: shop image - store in firebase storage)
| (Data: upload image - store in firebase storage)
| (Layout: detail_page_layout.xml)
|
+- Function: CameraActivity - (UPLOAD) button
| (layout: camera_page_layout.xml)
| (Data: Upload data(image) would upload to and store on firebase storage)
| (Function: Camera is used)
| (Function: phone image gallery is used)
|
+- Function: CommentActivity - (COMMENT) button
| (layout: camera_page_layout.xml)
| (Data: comment - store in firebase realtime database)
| (Adapter layout: detail_page_item_list)
| (Function: comment - user can leave message to shop)
| 
+- Function: MapsActivity - (MAP) button
| (layout: maps_page_layout.xml)
| (Data: shop location - store in firebase realtime database)
| (Adapter layout: detail_page_item_list)
| (Function: Map - for showing shop location in the map & current location would used)
