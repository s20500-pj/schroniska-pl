//todo this is simple workaround. need to integrate react with spring.
import React from "react";
import {CheckCircleIcon} from "@heroicons/react/20/solid";

export default function PayUConfirmationPage() {
    return (
        <div className="min-h-screen bg-gray-50 flex flex-col justify-center ">
            <div className="flex items-center justify-center mx-auto h-12 w-12 rounded-full bg-green-100">
                <CheckCircleIcon className="h-6 w-6 text-green-600"/>
            </div>
            <div className="mt-4">
                <div className="text-center">
                    <h3 className="text-lg leading-6 font-medium text-gray-900">Dziękujemy za dokonanie wpłaty.
                        Teraz oczekujemy na zatwierdzenie płatności przez wybrane schronisko. <p>
                            Po potwierdzeniu płatności, otrzymasz e-mail z potwierdzeniem.</p></h3>
                </div>
            </div>
        </div>
    );
}