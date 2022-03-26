	function Popup(data){
    	
    	 var mywindow = window.open('', 'my div', 'height=500,width=520,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=no,status=no');
    	    mywindow.document.write('<html><head><title>Ticket</title>');
    	    mywindow.document.write('<link rel="stylesheet" href="styles/ticket-styles.css" type="text/css" />');
    	    mywindow.document.write('</head><body >');
    	    mywindow.document.write(data);
    	    mywindow.document.write('</body></html>');
    	    mywindow.document.close(); // necessary for IE 10 or superior

    	    myDelay = setInterval(checkReadyState, 10);

    	    function checkReadyState() {
    	        if (mywindow.document.readyState == 'complete') {
    	            clearInterval(myDelay);
    	            mywindow.focus(); // necessary for IE 10 or superior
					mywindow.print();
					mywindow.close();
    	        }
    	    }

    	    return true;
    	
	}