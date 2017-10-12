<?php
$response = array();
require("../db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_POST['user_phone'])&&isset($_POST['user_schoolname'])) {
    $user_phone = $_POST['user_phone'];
    $user_schoolname = $_POST['user_schoolname'];
    $sql="update user_information set user_schoolname='$user_schoolname' where user_phone='$user_phone'";
	$result=mysql_query($sql);
   if ($result) {
        $response["success"] = 1;
        $response["message"] = "YES";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "NO";
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "NO";
    echo json_encode($response);
}

//根据手机号修改信息
//$name='xbw';
//$phone='17863963884';
//$sql="update user_information set user_name='$name' where user_phone='$phone'";
//mysql_query($sql,$conn);
//根据手机号查找信息
//$xbw='xbw12138';
//$sql="select user_phone,user_signtime from user_information where user_password='$xbw'";
//$sql="select     user_name,user_sign,user_image_head,user_sex,user_age,user_schoolname,user_talk,user_signtime //from     user_information where user_password='$xbw'";
//$result=mysql_query($sql,$conn);
//$row=mysql_fetch_row($result,MYSQL_ASSOC);
//echo $row[0];
//$row=mysql_fetch_array($result)
//while($row=mysql_fetch_array($result))
//{
//  print($row[1]);
//}
?>