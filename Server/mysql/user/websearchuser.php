<?php
$response = array();
require("../db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_GET['user_phone'])) {
    $user_phone = $_GET['user_phone'];
    $sql="select     user_name,user_sign,user_image_head,user_sex,user_age,user_schoolname,user_talk,user_signtime,user_bg,user_vip from     user_information where user_phone='$user_phone'";
	$result=mysql_query($sql);
    if ($row=mysql_fetch_row($result)) {
	$array = array( 
	'message'=>1,
	'user_name'=>$row[0], 
	'user_sign'=>$row[1], 
	'user_image_head'=>$row[2],
	'user_sex'=>$row[3], 
	'user_age'=>$row[4], 
	'user_schoolname'=>$row[5], 
	'user_talk'=>$row[6], 
	'user_signtime'=>$row[7], 
	'user_bg'=>$row[8],
	'user_vip'=>$row[9],
	); 
        echo json_encode($array);

    } else {
	$array = array( 
	'message'=>'NO',
	'success'=>0, 
	); 
        echo json_encode($array);
    }
} else {
	$array = array( 
	'message'=>'NO',
	'success'=>0, 
	); 
echo json_encode($array);
}
?>