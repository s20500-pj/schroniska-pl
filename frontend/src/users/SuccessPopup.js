import React from "react";
import {useNavigate} from "react-router-dom";

function SuccessPopup({message}) {

    let navigate = useNavigate();
    const handleOkClick = () => {
        navigate("/");
    };

    return (
        <div className="popup-container">
            <div className="popup">
                <p>{message}</p>
                <button onClick={handleOkClick}>OK</button>
            </div>
        </div>
    );
}

export default SuccessPopup;
