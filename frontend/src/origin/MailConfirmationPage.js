import React from "react";
import {useParams} from "react-router-dom";


//todo this is simple workaround. need to integrate react with spring.
export default function MailConfirmationPage() {
    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className=" px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-5 h-fit ">
                    Gratulacje! Twój mail został potwierdzony. Możesz się zalogować.
                </h2>
            </div>
        </div>
    );
}


