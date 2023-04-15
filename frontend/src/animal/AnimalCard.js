import React, {useEffect, useMemo, useState} from "react";
import axios from "axios";
export default function AnimalCard({data}) {
    axios.defaults.withCredentials = true;


    return (
        <div className="md:h-fit sm:h-fit ">
            <h3 className="font-display text-center text-brown font-bold text-2xl">Zwierzęta w schroniskach</h3>
            <div className="flex flex-wrap h-fit justify-start">
                {data.map(data => {
                    return (
                        <div className="p-5 flex " key={data.id}>
                            <div className="bg-white rounded-3xl shadow-xl overflow-hidden hover:scale-105 ">
                                <div className="w-[200px] ">
                                    <img src={data.imagePath} alt="Zdjecie zwierzęcia" className="object-cover h-48 w-64" />
                                    <div className="bg-orange p-4 w-30 sm:p-6 ">
                                        <p className="text-[22px] font-bold">{data.name}</p>
                                        <div className="flex">
                                            <p className="font-bold text-gray-700 text-[16px] mb-1">{data.sex}</p>
                                        </div>
                                        <p className="font-[15px]">{data.age}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )
                })
                }
            </div>

        </div>

    )
}
