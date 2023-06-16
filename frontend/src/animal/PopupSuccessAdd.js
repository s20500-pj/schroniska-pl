import React from "react";
import {useNavigate} from "react-router-dom";

function PopupSuccessAdd({setOpenModal}) {
    const navigate = useNavigate();

    return (<>
            <div
                className="justify-center items-center flex overflow-x-hidden overflow-y-auto fixed inset-0 z-50
                outline-none focus:outline-none font-display w-500">
                <div className="relative w-auto my-6 mx-auto max-w-3xl">
                    <div
                        className="border-0 rounded-lg shadow-lg relative flex flex-col w-full bg-white outline-none focus:outline-none">
                        <div
                            className="flex items-start justify-between p-5 border-b border-solid border-slate-200 rounded-t">
                            <h3 className="text-2xl font-semibold">
                                Dodanie zwierzęcia </h3>
                            <button
                                className="p-1 ml-auto bg-transparent border-0 text-black opacity-5 float-right text-3xl leading-none font-semibold outline-none focus:outline-none"
                                onClick={() => setOpenModal(false)}
                            >
                    <span
                        className="bg-transparent text-black opacity-5 h-6 w-6 text-2xl block outline-none focus:outline-none">
                      ×
                    </span>
                            </button>
                        </div>
                        <div className="relative p-6 flex-auto">
                            <p className="my-4 text-orange text-lg leading-relaxed">
                                Dodano zwierzaka pomyślnie </p>
                        </div>
                        <div
                            className="flex items-center justify-end p-6 border-t border-solid border-slate-200 rounded-b">
                            <button
                                className="bg-orange text-white active:bg-orange font-display uppercase text-sm px-6 py-3 rounded-2xl shadow hover:shadow-lg outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                                type="button"
                                onClick={() => {
                                    setOpenModal(false);
                                    navigate('/shelterAnimalList')
                                }}
                            >
                                Zamknij
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div className="opacity-25 fixed inset-0 z-40 bg-black"></div>
        </>
    );
}

export default PopupSuccessAdd;
