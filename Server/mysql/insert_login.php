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

//$sql ="select * from user_information "; //SQL���
//$result = mysql_query($sql,$conn); //��ѯ
//while($e=mysql_fetch_assoc($result))  
//        $output[]=$e;  
//print(json_encode($output)); 
//if (!$conn){
//    die("�������ݿ�ʧ�ܣ�" . mysql_error());
//}
//else{
// die("�������ݿ�ɹ�");
//}	

//$password = md5("123456");		//ԭʼ���� 12345 �������ܺ�õ����ܺ�����
//$regdate = time();			//�õ�ʱ���
//$sql_s = "INSERT INTO user_information(user_phone, user_password, user_signtime)VALUES('17854259542', //'xbw12138', '2016-03-12')";
//exit($sql);	                        //�˳����򲢴�ӡ SQL ��䣬���ڵ���

//$results = mysql_query($sql_s,$conn); //��ѯ
//while($e=mysql_fetch_assoc($results))  
//        $output[]=$e;  
//print(json_encode($output)); 
//if(!mysql_query($sql_s,$conn)){
//    echo "�������ʧ�ܣ�".mysql_error();
//} else {
//    "������ݳɹ���";
//}
?>
