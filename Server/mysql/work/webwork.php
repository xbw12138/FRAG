<?php
$response = array();
require("../db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_GET['page'])) {
    $page = $_GET['page'];
    $sql="select     start_time,end_time,work_describe,work_position ,is_finish,ID,send_time from  shijianfabu limit $page,2";
	if($result=mysql_query($sql)){
		while($row=mysql_fetch_array($result)){
			$array[] = array( 
			'message'=>'YES',
			'start_time'=>$row[0], 
			'end_time'=>$row[1], 
			'work_describe'=>$row[2],
			'work_position'=>$row[3], 
			'is_finish'=>$row[4], 
			'ID'=>$row[5],
			'send_time'=>$row[6],
			); 
		}
		$resultz = empty($array);
		if($resultz){
			$array = array( 
			'message'=>'数据加载完毕',
			'success'=>0, 
			); 
		}
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