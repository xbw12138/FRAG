<?php
//$array[] = array();
//$array[] = $row[0];
//$array[] = $row['user_name'] . " " . $row['user_phone'];
//$array[$row['user_name']] = $row[0];
//echo $row['user_name'] . " " . $row['user_phone'];
//echo "<br />";
//if($row=mysql_fetch_row($result)){
//$array=array(
//	'a'=>$row[0],
//	'b'=>$row[1],
//	);
//echo json_encode($array);
//}
require("db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_POST['user_phone'])) {
    $user_phone = $_POST['user_phone'];
    $sql="select user_review_num,user_talk,user_agree,user_browser,user_talk_img,talk_time from talk_information where     user_phone='$user_phone'";
    $result=mysql_query($sql,$conn);
	while($row = mysql_fetch_array($result))
	{
		$array[] = array( 
		'messages'=>'Success',
		'success'=>1, 
		'user_talk'=>$row['user_talk'],
		'user_agree'=>$row['user_agree'],
		'user_browser'=>$row['user_browser'],
		'user_talk_img'=>$row['user_talk_img'],
		'talk_time'=>$row['talk_time'],
		'user_review_num'=>$row['user_review_num'],
		); 	
	}
    echo json_encode($array);
}else{
	$array[] = array( 
	'messages'=>'Required field(s) is missing',
	'success'=>0, 
	); 
	echo json_encode($array);
}
?>




