<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Group extends CI_Controller {

	public function __construct() {
		parent::__construct();

		if (!isset($_SESSION)) {
			session_start(); 
		}
		
		$this->load->model('group/jennifer_module');
	}

	function index() {
		$query_test = $this->jennifer_module->get_users();
		$some_data = array();
		$some_data['extra_data'] = $query_test;
		$this->load->view('group/test_jennifer', $some_data);
	}

	function page_one() {
		$get_group_test = $this->jennifer_module->get_group_test();
		echo "<pre>";
		print_r($get_group_test);
		echo "</pre>";
	}
}
?>