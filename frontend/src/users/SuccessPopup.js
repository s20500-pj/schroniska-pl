import React from "react";
import {useNavigate} from "react-router-dom";

function SuccessPopup() {

    let navigate = useNavigate();
    const handleOkClick = () => {
        navigate("/");
    };

    return (
        <div className="popup-container">
            <div className="popup">
                <p>Rejestracja pomyślna. Wysłaliśmy Tobie mail z linkem aktywacyjnym w celu weryfikacji konta. Podążaj
                    za wskazówkami zawartymi w mailu. Dziękujemy za rejestrację.</p>
                <button onClick={handleOkClick}>OK</button>
            </div>
        </div>
    );
}

export default SuccessPopup;
