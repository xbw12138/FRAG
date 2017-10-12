<?php
$response = array();
require("../db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_POST['user_phone']) && isset($_POST['start_time'])&&isset($_POST['end_time'])&&isset($_POST['work_describe'])&&isset($_POST['work_position'])&&isset($_POST['send_time'])) {
    $user_phone = $_POST['user_phone'];
    $start_time = $_POST['start_time'];
    $end_time = $_POST['end_time'];	
    $work_describe = $_POST['work_describe'];
    $work_position = $_POST['work_position'];
    $send_time = $_POST['send_time'];
    $result = "INSERT INTO shijianfabu (user_phone, start_time, end_time,work_describe,work_position,send_time) VALUES('$user_phone', '$start_time', '$end_time', '$work_describe', '$work_position', '$send_time')";
mysql_query($result);
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

?>
