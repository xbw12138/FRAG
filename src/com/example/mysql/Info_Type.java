package com.example.mysql;

public class Info_Type {
	
	public enum INFOTYPE{
        
		 //�û�
        user_name {public String getUrl(){return "user/change_nickname.php";}public INFOTYPE getType(){return user_name;}public String getName(){return "user_name";}},
        
        user_age {public String getUrl(){return "user/change_age.php";}public INFOTYPE getType(){return user_age;}public String getName(){return "user_age";}},
        
        user_sex {public String getUrl(){return "user/change_sex.php";}public INFOTYPE getType(){return user_sex;}public String getName(){return "user_sex";}},
        
        user_schoolname {public String getUrl(){return "user/change_schoolname.php";}public INFOTYPE getType(){return user_schoolname;}public String getName(){return "user_schoolname";}},
        
        user_sign {public String getUrl(){return "user/change_sign.php";}public INFOTYPE getType(){return user_sign;}public String getName(){return "user_sign";}},
        
        user_vip {public String getUrl(){return "user/change_vip.php";}public INFOTYPE getType(){return user_vip;}public String getName(){return "user_vip";}},
		//�¼�
        start_time {public String getUrl(){return "work/change_start_time.php";}public INFOTYPE getType(){return start_time;}public String getName(){return "start_time";}},
        
        end_time {public String getUrl(){return "work/change_end_time.php";}public INFOTYPE getType(){return end_time;}public String getName(){return "end_time";}},
        
        work_describe {public String getUrl(){return "work/change_describe.php";}public INFOTYPE getType(){return work_describe;}public String getName(){return "work_describe";}},
        
        work_position {public String getUrl(){return "work/change_position.php";}public INFOTYPE getType(){return work_position;}public String getName(){return "work_position";}};
        
        
       
        public abstract INFOTYPE getType();
        public abstract String getName();
        public abstract String getUrl();
	}

}
