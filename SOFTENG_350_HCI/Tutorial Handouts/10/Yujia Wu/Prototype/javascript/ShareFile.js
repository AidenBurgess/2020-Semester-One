// toggle between sharing individual file and sharing all related files
function showOrHideRelatedFiles( buttonId ) {
	// get the relationship graph of the file that user wants to share
	const graph = document.getElementById( "multiple-file" );

	// get the id of the toggle button clicked
	const button = document.getElementById( buttonId );

	if( graph.style.display === "none" ) {
		// if the graph is already displayed, hide the graph
		graph.style.display = "block";
		button.innerText = "Share one file only";

	} else {
		// if the graph is hidden, show it
		graph.style.display = "none";
		button.innerText = "Share all files containing the same tag";
	}
}

// close the share file popup
function hidePopup() {

	// hide the relationship graph - ensure a fresh start the next time
	const graph = document.getElementById( "multiple-file" );
	console.log(graph)
	console.log(graph.style.display)
	if( graph.style.display !== "none" ) {
		showOrHideRelatedFiles( "share-all-file-button" );
	}

	// close the popup
	document.getElementById("share-file-popup").style.display = "none";
}

function showPopup() {
	// show the share file popup
	document.getElementById("share-file-popup").style.display = "block";
}

// displayed after user clicks share button
function fileSuccessfullySharedAlert( innerText ) {
	// show the successful-shared-alert popup
	const message = document.getElementById( "alert-message" );
	message.innerText = innerText;
	document.getElementById( "successful-shared-alert" ).style.display = "block";

	// hide the popup after 2 seconds
	setTimeout(function(){
		document.getElementById( "successful-shared-alert" ).style.display = "none";
	}, 2000);
}


