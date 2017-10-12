package com.example.frag_update;

public class UpdateInfo
{
        private String version;
        private String description;
        private String url;
        private String title;
    	public String function_open_price;
    	public String mysql_url;//数据库url;
        public String getVersion()
        {
                return version;
        }
        public void setVersion(String version)
        {
                this.version = version;
        }
        public String getDescription()
        {
                return description;
        }
        public void setDescription(String description)
        {
                this.description = description;
        }
        public String getUrl()
        {
                return url;
        }
        public void setUrl(String url)
        {
                this.url = url;
        }
        public void setTitle(String title)
        {
        	this.title=title;
        }
        public String getTitle()
        {
        	return title;
        }
        public void setfunction_open_price(String function_open_price)
        {
        	this.function_open_price=function_open_price;
        }
        public String getfunction_open_price()
        {
        	return function_open_price;
        }
        public String getmysql_url()
        {
                return mysql_url;
        }
        public void setmysql_url(String mysql_url)
        {
                this.mysql_url = mysql_url;
        }
}
