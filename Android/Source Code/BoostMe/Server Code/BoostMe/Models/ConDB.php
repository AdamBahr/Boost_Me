<?php
error_reporting(0);
/**
 * Description of ConDB
 *
 * @author admin@3embed
 */
class ConDB {
    //Put your Database configuration here
        
        private $serverName = "localhost"; //serverName\instanceName
        private $userName = "root";
        private $pass = "";
        private $database = "tinderclone";
		
	/*	
		private $serverName = ""; //serverName\instanceName
        private $userName = "";
        private $pass = "";
        private $database = "test_db";*/
		
        public $conn;
        public $flag_conn;
        public function __construct(){
                 $this->conn = mysql_connect($this->serverName, $this->userName,$this->pass);
                 if($this->conn) {
                     if(mysql_select_db($this->database,$this->conn)){
                               $this->flag_conn = 0;
                     }else{
                               $this->flag_conn = 1;
                               die( print_r( mysql_errors(), true));
                     }
                }else{
                     $this->flag_conn = 1;
                     die( print_r( mysql_error(), true));
                }
        }
        
        public function close($db){
           // mysql_close($db);
        }
}
?>
