<?php if (!defined('BASEPATH')) exit('No direct script access allowed');


/**
 * Helps user logs in
 */
class Login extends CI_Controller {

	public function __construct() {
		parent::__construct();
		if (!isset($_SESSION)) {
			session_start(); 
		}
		$this->load->model('login/login_model');
	}

	function index() {
		$this->load->view('login/login_page');
	}

	function login_submit() {
		$this->session->set_userdata('username', $_POST['username']);
		$this->load->view('login/login_page');
		$post_output =
		"<pre>"
		.print_r($_POST, true).
		"</pre>";
		$this->output->append_output($post_output);
	}

	function logout() {
		$this->session->set_userdata('username', '');
		$this->session->userdata = array();
		$this->session->sess_destroy();
		redirect(base_url(), 'refresh');
	}

	function bob() {
		echo "bob!";
	}
}
?>