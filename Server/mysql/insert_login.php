<?php
$response = array();
require("db_config.php");
$conn=mysql_connect($mysql_server_name,$mysql_username,$mysql_password) or die("error connecting") ;
mysql_query("set names 'utf8'"); 
mysql_select_db($mysql_database);
if (isset($_POST['user_phone']) && isset($_POST['user_password'])&&isset($_POST['user_signtime'])&&isset($_POST['user_vip'])) {
    $user_phone = $_POST['user_phone'];
    $user_password = $_POST['user_password'];
    $user_signtime = $_POST['user_signtime'];	
    $user_vip = $_POST['user_vip'];
    $result = "INSERT INTO user_information(user_phone, user_password, user_signtime, user_vip) VALUES('$user_phone', '$user_password', '$user_signtime', '$user_vip')";
//$sql = "INSERT INTO user_information(user_phone, user_password, user_signtime)VALUES('17854259542', 'xbw12138',
// '2016-03-12')";
mysql_query($result);
if ($result) {
        $response["success"] = 1;
        $response["message"] = "Success!";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}

//$sql ="select * from user_information "; //SQL语句
//$result = mysql_query($sql,$conn); //查询
//while($e=mysql_fetch_assoc($result))  
//        $output[]=$e;  
//print(json_encode($output)); 
//if (!$conn){
//    die("连接数据库失败：" . mysql_error());
//}
//else{
// die("连接数据库成功");
//}	

//$password = md5("123456");		//原始密码 12345 经过加密后得到加密后密码
//$regdate = time();			//得到时间戳
//$sql_s = "INSERT INTO user_information(user_phone, user_password, user_signtime)VALUES('17854259542', //'xbw12138', '2016-03-12')";
//exit($sql);	                        //退出程序并打印 SQL 语句，用于调试

//$results = mysql_query($sql_s,$conn); //查询
//while($e=mysql_fetch_assoc($results))  
//        $output[]=$e;  
//print(json_encode($output)); 
//if(!mysql_query($sql_s,$conn)){
//    echo "添加数据失败：".mysql_error();
//} else {
//    "添加数据成功！";
//}
?>
