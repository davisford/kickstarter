
$(document).ready(function() {
	
  // fetch the widgets when the page loads
  fetchWidgets();

  // post to /service/hello/{name} when they click blue button
  $('#bigBlueButton').click(function() {
	var who = $('#hiTextBox').val();
	if(!who) { who = "Mr. No Name"; }
    $.ajax({
    	url: '/service/hello/'+who,
    	dataType: 'text',
    	success: function(data) {
    	  alert(data);
    	},
    	error: function(xhr, status, ex) {
    	  console.log(status);
    	}
    });
  });
  
  // ensure only numeric input on Id text input
  $('#idTextBox').keydown(function (event) {
	  // Allow: backspace, delete, tab, escape, and enter
      if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 || 
           // Allow: Ctrl+A
          (event.keyCode == 65 && event.ctrlKey === true) || 
           // Allow: home, end, left, right
          (event.keyCode >= 35 && event.keyCode <= 39)) {
               // let it happen, don't do anything
               return;
      }
      else {
          // Ensure that it is a number and stop the keypress
          if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
              event.preventDefault(); 
          }   
      }
  });
  
  // click handler for save widget
  $('#saveWidgetButton').click(function (event) {
	  // prevent default b/c we do it via ajax and form post will reload page
	  event.preventDefault();
	  var widget = { id: $('#idTextBox').val(), name: $('#nameTextBox').val() };
	  if(!widget.id || !widget.name) {
		  alert("You have to fill out the form first");
		  return;
	  }
	  $.ajax({
		  url: '/service/widgets/'+widget.id,
		  type: 'POST',
		  data: JSON.stringify(widget),
		  contentType: 'application/json',
		  success: function(data) {
			  console.log(data);
			  // refresh the widgets list
			  fetchWidgets();
		  },
		  error: function(xhr, status, ex) {
			  console.log(status);
		  }
	  });
  });
  
  $('#onButton').click(function() {
	  $.ajax({
		  url: '/service/toggle/1'
	  });
  });
  
  $('#offButton').click(function() {
	  $.ajax({
		  url: '/service/toggle/0'
	  });
  });
  
  // fetch widgets and update list
  function fetchWidgets() {
	  $.getJSON('/service/widgets/', function(data) {
		 var items = [];
		 $.each(data, function(key, val) {
			 $.each(val, function(k, v) {
				 $.each(v, function(a, b) {
					 items.push('<li>Id:'+b.id+', Name:'+b.name+'</li>');
				 });
				 
			 });
			 
		 });
		 $('#widgetList').html(items.join(''));
	  });
  }
  
});