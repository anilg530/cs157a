<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Default_test extends CI_Controller {

	public function __construct() {
		parent::__construct();

		if (!isset($_SESSION)) {
			session_start(); 
		}
		
		$this->load->model('default_test/default_test_model');
	}

	function index() {
		$page_data = array();
		$page_data['users_array'] = $this->default_test_model->get_users();
		$this->load->view('default_page', $page_data);
	}
}
?>