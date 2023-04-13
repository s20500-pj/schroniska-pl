import React from 'react';

function Image(props) {
    const imagePath = props.path;

    return (
        <img src={imagePath} alt="Tutaj powinien pojawić się obrazek"/>
    );
}

export default Image;