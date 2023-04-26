import React, {useEffect, useState} from "react";

function Update() {
    const type = localStorage.getItem('userType')

    return (
        <div>
            <h2> UPDATE</h2>
        <form>
            <div>
                <label htmlFor="firstName"> IMIE</label>
                <input type="text" firstName="firstName" placeholder="Zmien imie"/>
            </div>
            <button>Update</button>
        </form>
    </div>
    )
}

export default Update;