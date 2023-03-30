import React from "react";
import {useNavigate} from "react-router-dom";

function ErrorPopup() {

    let navigate = useNavigate();
    const handleOkClick = () => {
        navigate("/");
    };
    //TODO do zrobienia popup przyjmujący dane wejściowe w celu wyświetlania komunikatu uzytkownikowi
    return (
        <div className="popup-container">
            <div className="popup">
                <p>ERROR</p>
                <button onClick={handleOkClick}>OK</button>
            </div>
        </div>
    );
}

export default ErrorPopup;
