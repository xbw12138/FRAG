<?php
$response = array();
require("../db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_POST['ID'])) {
    $ID = $_POST['ID'];
    $sql="select     start_time,end_time,work_describe,work_position ,is_finish,send_time from     shijianfabu where ID='$ID'";
	$result=mysql_query($sql);
    if ($row=mysql_fetch_row($result)) {
	$array = array( 
	'message'=>'YES',
	'start_time'=>$row[0], 
	'end_time'=>$row[1], 
	'work_describe'=>$row[2],
	'work_position'=>$row[3], 
	'is_finish'=>$row[4], 
	'send_time'=>$row[5],
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