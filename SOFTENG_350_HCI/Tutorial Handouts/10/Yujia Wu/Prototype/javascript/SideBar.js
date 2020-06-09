// expand/collapse the folder button in the side bar
// because this is a prototype, this function only works on softeng350 button
function ToggleExpandContent(buttonId, expandContactId ) {
	const content = document.getElementById( expandContactId );
	const button = document.getElementById( buttonId );
	const flexContainer = button.getElementsByClassName( "flex-container" )[0];

	// if the button is not expanded
	if( content.style.display === "none" ) {
		// display the expanded result
		content.style.display = "block";

		// change the direction of the triangle beside the folder
		const triangle = flexContainer.getElementsByClassName( "triangle-right" )[0];
		triangle.classList.replace( "triangle-right", "triangle-bottom" );

		// change the padding so the new triangle lined up with the one on the top
		flexContainer.style.paddingLeft = "10px";

	} else {
		// if the button is already expanded, collapse the content
		content.style.display = "none";

		// change the direction of the triangle beside the folder
		const triangle = flexContainer.getElementsByClassName( "triangle-bottom" )[0];
		triangle.classList.replace( "triangle-bottom", "triangle-right" );

		// change the padding back
		flexContainer.style.paddingLeft = "20px";
	}
}

// change background color of the button clicked
// buttonId: id of the button clicked
function changeSideBarButtonColor( buttonId ) {

	// get all buttons in the side bar
	const buttons = document.getElementsByClassName( "side-bar-button" );

	for( let i = 0; i < buttons.length; i++ ) {
		// reset the background color
		buttons[i].style.backgroundColor = "#b3d9ff";
	}

	// update the background color of the button clicked
	document.getElementById( buttonId ).style.backgroundColor = "#87CEEB";
}