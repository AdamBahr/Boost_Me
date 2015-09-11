<?php

require_once 'Models/ConDB.php';
$db = new ConDB();
?>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title></title>
        <style>
            .form_row{height: 40px;}
            .form_label{display: inline-block;width: 250px;text-align: right;}
            .form_field{display: inline;}
            .div_service{margin:3% 0;}
            .list_1 li{padding: 10px 0;}
            .list_1 li a{color: #1383e4;font-weight: bold;font-family: verdana;}
        </style>
    </head>
    <body>
        <div id="top">&nbsp;</div>
        <div style="position:fixed;right: 5%;top:40%;">
            <a href="#top"><input type="button" value="Top"/></a>
        </div>
        <h2>Tinder clone services</h2>
        <ol type="1" class="list_1">
            <li><a href="#login_service">Login</a> (login)</li>
            <!--            <li><a href="#update_entity_service">Update Social Details ()</a></li>-->
            <li><a href='#update_preferences_service'>Update Preferences</a> (updatePreferences)</li>
            <li><a href='#upload_image_service'>Upload image</a> (uploadImage)</li>
            <li><a href='#delete_image_service'>Delete image</a> (deleteImage)</li>
            <li><a href='#insert_likes_service'>Set User Likes</a> (setLikes)</li>
            <li><a href="#find_matches_service">Find Matches</a> (findMatches)</li>
            <li><a href="#invite_action_service">Invite Action</a> (inviteAction)</li>
            <li><a href="#block_user_service">Block User</a> (blockUser)</li>
            <li><a href="#get_profile_likes_service">Get users who liked the profile</a> (getProfileMatches)</li>
            <li><a href="#get_notification_service">Get Notifications</a> (getNotifications)</li>
            <li><a href="#get_profile_service">Get User Profile</a> (getProfile)</li>
            <li><a href="#get_preferences_service">Get Preferences</a> (getPreferences)</li>
            <li><a href="#edit_profile_service">Edit user profile</a> (editProfile)</li>
            <li><a href="#upldate_location_service">Update location</a> (updateLocation)</li>
            <li><a href="#send_message_service">Send Message</a> (sendMessage)</li>
            <li><a href="#get_chat_service">Get Chat history</a> (getChatHistory)</li>
            <li><a href="#get_message_service">Get chat message</a>(getChatMessage)</li>
            <li><a href="#update_session_service">Update session</a>(updateSession)</li>
            <li><a href="#logout_service">Logout user</a> (logout)<br><br></li>

            <li><a href="#push_test_service">Android Push Test</a> (testAndiPush)</li>
            <li><a href="#push_test_aple_service">IOS Push Test</a> (testIosPush)</li>
        </ol>

        <div id="login_service" class="div_service">
            <form action="process.php/login" method="post">
                <h3>Social Login</h3>
                <div class="form_row">
                    <div class="form_label">First Name *: </div>
                    <div class="form_field"><input type="text" name="ent_first_name" />name= "ent_first_name"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Last Name : </div>
                    <div class="form_field"><input type="text" name="ent_last_name" />name="ent_last_name"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">FB Id: </div>
                    <div class="form_field"><input type="text" name="ent_fbid" />name="ent_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">G+ id : </div>
                    <div class="form_field"><input type="text" name="ent_gp_id" />name="ent_gp_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Email *: </div>
                    <div class="form_field"><input type="text" name="ent_email" />name="ent_email"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Sex *: </div>
                    <div class="form_field"><input type="radio" name="ent_sex" value="1" checked />Male&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="ent_sex" value="2"  />Female &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; name="ent_sex"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">City : </div>
                    <div class="form_field"><input type="text" name="ent_city" />name="ent_city"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Country: </div>
                    <div class="form_field"><input type="text" name="ent_country" />name="ent_country"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Current Latitude : </div>
                    <div class="form_field"><input type="text" name="ent_curr_lat" />name="ent_curr_lat"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Current Longitude : </div>
                    <div class="form_field"><input type="text" name="ent_curr_long" />name="ent_curr_long"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Tag Line : </div>
                    <div class="form_field"><input type="text" name="ent_tag_line" />name="ent_tag_line"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Personal Description : </div>
                    <div class="form_field"><input type="text" name="ent_pers_desc" />name="ent_pers_desc"</div>
                </div>
                <div class="form_row" style="margin-bottom:20px;">
                    <div class="form_label">Date Of Birth: (YYYY-MM-DD)</div>
                    <div class="form_field"><input type="text" name="ent_dob" />name="ent_dob"</div>
                </div>
                <div class="form_row" style="margin-bottom:20px;">
                    <div class="form_label">Preference sex  ( 1 - male, 2 - female, 3 - both): </div>
                    <div class="form_field"><input type="text" name="ent_pref_sex" />name="ent_pref_sex"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference lower age : </div>
                    <div class="form_field"><input type="text" name="ent_pref_lower_age" />name="ent_pref_lower_age"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference upper age : </div>
                    <div class="form_field"><input type="text" name="ent_pref_upper_age" />name="ent_pref_upper_age"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference radius : </div>
                    <div class="form_field"><input type="text" name="ent_pref_radius" />name="ent_pref_radius"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Likes :(',' separated if many) </div>
                    <div class="form_field"><input type="text" name="ent_likes" />name="ent_likes"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Push Token *: </div>
                    <div class="form_field"><input type="text" name="ent_push_token" />name="ent_push_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">QBID *: </div>
                    <div class="form_field"><input type="text" name="ent_qbid" />name="ent_qbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device type *: </div>
                    <div class="form_field">
                        <select name="ent_device_type">
                            <?php 
            $getDevTypesQry = "select * from t_dev_type";
            $getDevTypesRes = mysql_query($getDevTypesQry, $db->conn);
            while ($dev = mysql_fetch_assoc($getDevTypesRes)) { ?>
                                <option value="<?php echo $dev['dev_id']; ?>"><?php echo $dev['name'] . '(' . $dev['dev_id'] . ')'; ?></option>
                            <?php } ?>
                        </select>name="ent_device_type"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Authentication type *: </div>
                    <div class="form_field">
                        <select name="ent_auth_type">
                            <?php 
             $getAuthTypesQry = "select * from t_authtypes";
            $getAuthTypesRes = mysql_query($getAuthTypesQry, $db->conn);
            echo $getAuthTypesRes;
                            while ($dev = mysql_fetch_assoc($getAuthTypesRes)) { ?>
                                <option value="<?php echo $dev['authType_id']; ?>"><?php echo $dev['authName'] . '(' . $dev['authType_id'] . ')'; ?></option>
                            <?php } ?>
                        </select>name="ent_auth_type"
                    </div>
                </div>
                <div class="form_row">
                    <div class="form_label">*-marked are mandatory</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" />name="ent_submit"</div>
                </div>
            </form>
        </div>

        <div id="update_preferences_service" class="div_service">
            <form action="process.php/updatePreferences" method="post">
                <h3>Update preferences</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">User sex  ( 1 - male, 2 - female)*: </div>
                    <div class="form_field"><input type="text" name="ent_sex" />name="ent_sex"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference sex  ( 1 - male, 2 - female, 3 - both)*: </div>
                    <div class="form_field"><input type="text" name="ent_pref_sex" />name="ent_pref_sex"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference lower age *: </div>
                    <div class="form_field"><input type="text" name="ent_pref_lower_age" />name="ent_pref_lower_age"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference upper age *: </div>
                    <div class="form_field"><input type="text" name="ent_pref_upper_age" />name="ent_pref_upper_age"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference radius *: </div>
                    <div class="form_field"><input type="text" name="ent_pref_radius" />name="ent_pref_radius"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Preference tim *: </div>
                    <div class="form_field"><input type="text" name="ent_pref_time" />name="ent_pref_time"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="upload_image_service" class="div_service">
            <form action="process.php/uploadImage" method="post">
                <h3>Upload image :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Profile Image Url *: </div>
                    <div class="form_field"><input type="text" name="ent_prof_url" />name="ent_prof_url"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Other Image Urls (, separated if many) : </div>
                    <div class="form_field"><input type="text" name="ent_other_urls" />name="ent_other_urls"</div>
                </div>
<!--                <div class="form_row">
                    <div class="form_label">Flag *: </div>
                    <div class="form_field"><input type="text" name="ent_image_flag" />name="ent_image_flag"&nbsp;&nbsp;1 - Profile pic, 2 - other</div>
                </div>-->
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="delete_image_service" class="div_service">
            <form action="process.php/deleteImage" method="post">
                <h3>Delete image :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Image Name *: </div>
                    <div class="form_field"><input type="text" name="ent_image_name" />name="ent_image_name"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Flag *: </div>
                    <div class="form_field"><input type="text" name="ent_image_flag" />name="ent_image_flag"&nbsp;&nbsp;1 - Profile pic, 2 - other</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_preferences_service" class="div_service">
            <form action="process.php/getPreferences" method="post">
                <h3>Get Preferences :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="insert_likes_service" class="div_service">
            <form action="process.php/setLikes" method="post">
                <h3>Set User Likes :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Likes *:(',' separated if many) </div>
                    <div class="form_field"><input type="text" name="ent_likes" />name="ent_likes"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="find_matches_service" class="div_service">
            <form action="process.php/findMatches" method="post">
                <h3>Find Matches :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="invite_action_service" class="div_service">
            <form action="process.php/inviteAction" method="post">
                <h3>Invite Action :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Invitee FB Id *: </div>
                    <div class="form_field"><input type="text" name="ent_invitee_fbid" />name="ent_invitee_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Action  (1 - Like, 2 - Dislike)*: </div>
                    <div class="form_field"><input type="radio" name="ent_user_action" value="1"/>Like&nbsp;&nbsp;<input type="radio" name="ent_user_action" value="2"/>Dislike&nbsp;&nbsp;name="ent_user_action"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="block_user_service" class="div_service">
            <form action="process.php/blockUser" method="post">
                <h3>Block User :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Blocking user FB Id *: </div>
                    <div class="form_field"><input type="text" name="ent_user_fbid" />name="ent_user_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Blocking Flag *: </div>
                    <div class="form_field"><input type="radio" name="ent_flag" value="3"/>Unblock&nbsp;&nbsp;<input type="radio" name="ent_flag" value="4"/>Block&nbsp;&nbsp;name="ent_flag"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_profile_likes_service" class="div_service">
            <form action="process.php/getProfileMatches" method="post">
                <h3>Get Users liked your profile :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div> 
                <div class="form_row">
                    <div class="form_label">GMT Date time (YYYY-MM-DD HH:MM:SS) *: </div>
                    <div class="form_field"><input type="text" name="ent_datetime" />name="ent_datetime"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_notification_service" class="div_service">
            <form action="process.php/getNotifications" method="post">
                <h3>Get Notifications :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_profile_service" class="div_service">
            <form action="process.php/getProfile" method="post">
                <h3>Get User profile :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">User FB Id *: </div>
                    <div class="form_field"><input type="text" name="ent_user_fbid" />name="ent_user_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="edit_profile_service" class="div_service">
            <form action="process.php/editProfile" method="post">
                <h3>Edit User profile :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Personal Description *: </div>
                    <div class="form_field"><input type="text" name="ent_pers_desc" />name="ent_pers_desc"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="upldate_location_service" class="div_service">
            <form action="process.php/updateLocation" method="post">
                <h3>Update location :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Current Latitude *: </div>
                    <div class="form_field"><input type="text" name="ent_curr_lat" />name="ent_curr_lat"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Current Longitude *: </div>
                    <div class="form_field"><input type="text" name="ent_curr_long" />name="ent_curr_long"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="send_message_service" class="div_service">
            <form action="process.php/sendMessage" method="post">
                <h3>Send message :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Receiver FB id *: </div>
                    <div class="form_field"><input type="text" name="ent_user_fbid" />name="ent_user_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Message *: </div>
                    <div class="form_field"><input type="text" name="ent_message" />name="ent_message"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_message_service" class="div_service">
            <form action="process.php/getChatMessage" method="post">
                <h3>Get chat message :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Message id *:(, separated if many) </div>
                    <div class="form_field"><input type="text" name="ent_msg_id" />name="ent_msg_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="get_chat_service" class="div_service">
            <form action="process.php/getChatHistory" method="post">
                <h3>Get Chat history :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Partner FB id *: </div>
                    <div class="form_field"><input type="text" name="ent_user_fbid" />name="ent_user_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Page Number (default - 1): </div>
                    <div class="form_field"><input type="text" name="ent_chat_page" />name="ent_chat_page"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>

        <div id="update_session_service" class="div_service">
            <form action="process.php/updateSession" method="post">
                <h3>Update session :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">FB id *: </div>
                    <div class="form_field"><input type="text" name="ent_fbid" />name="ent_fbid"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>
        
        <div id="logout_service" class="div_service">
            <form action="process.php/logout" method="post">
                <h3>Logout the user :</h3>
                <div class="form_row">
                    <div class="form_label">Session Token *: </div>
                    <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Device Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>


        <div id="push_test_service" class="div_service">
            <form action="process.php/testAndiPush" method="post">
                <h3>Test the push notifications for android:</h3>
                <div class="form_row">
                    <div class="form_label">Api key *: </div>
                    <div class="form_field"><input type="text" name="ent_api_key" />name= "ent_api_key"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Registration Id *: </div>
                    <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Message *: </div>
                    <div class="form_field"><input type="text" name="ent_message" />name="ent_message"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>


        <div id="push_test_aple_service" class="div_service">
            <form action="process.php/testIosPush" method="post" enctype="multipart/form-data">
                <h3>Test the push notifications for ios:</h3>
                <div class="form_row">
                    <div class="form_label">Ios Certificate *: </div>
                    <div class="form_field"><input type="file" name="ent_ios_cer" />name= "ent_ios_cer"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Certificate Pass *: </div>
                    <div class="form_field"><input type="text" name="ent_cer_pass" />name="ent_cer_pass"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Certificate Type *: </div>
                    <div class="form_field">
                        <input type="radio" name="ent_cer_type" value="1" />Production&nbsp;&nbsp;
                        <input type="radio" name="ent_cer_type" value="2" />Distribution&nbsp;&nbsp;name="ent_cer_type"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Push Token *: </div>
                    <div class="form_field"><input type="text" name="ent_push_token" />name="ent_push_token"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">Message *: </div>
                    <div class="form_field"><input type="text" name="ent_message" />name="ent_message"</div>
                </div>
                <div class="form_row">
                    <div class="form_label">&nbsp;</div>
                    <div class="form_field"><input type="submit" name="ent_submit" value="Submit" /></div>
                </div>
            </form>
        </div>




        <!--        <div id="update_entity_service" class="div_service">
                    <form action="process.php/updateEntity" method="post">
                        <h3>Update Social Details</h3>
                        <div class="form_row">
                            <div class="form_label">Session Token *: </div>
                            <div class="form_field"><input type="text" name="ent_sess_token" />name= "ent_sess_token"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">Device Id *: </div>
                            <div class="form_field"><input type="text" name="ent_dev_id" />name="ent_dev_id"</div>
                        </div>
                                        <div class="form_row">
                                            <div class="form_label">Api key : </div>
                                            <div class="form_field"><input type="text" name="ent_api_key" />name="ent_api_key"</div>
                                        </div>
                        <div class="form_row">
                            <div class="form_label">Profile Pic URL </div>
                            <div class="form_field"><input type="text" name="ent_prof_pic_url" />name= "ent_prof_pic_url"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">Pic URL 2</div>
                            <div class="form_field"><input type="text" name="ent_pic_2" />name= "ent_pic_2"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">Pic URL 3</div>
                            <div class="form_field"><input type="text" name="ent_pic_3" />name= "ent_pic_3"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">Pic URL 4</div>
                            <div class="form_field"><input type="text" name="ent_pic_4" />name= "ent_pic_4"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">Pic URL 5</div>
                            <div class="form_field"><input type="text" name="ent_pic_5" />name= "ent_pic_5"</div>
                        </div>
                        <div class="form_row">
                            <div class="form_label">*-marked are mandatory</div>
                            <div class="form_field"><input type="submit" name="ent_submit" value="Submit" />name="ent_submit"</div>
                        </div>
                    </form>
                </div>-->
    </body>
</html>