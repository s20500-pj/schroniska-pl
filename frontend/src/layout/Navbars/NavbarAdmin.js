import logo from "../LogoSchroniska-pl.png";
import {Link, useNavigate} from "react-router-dom";
import React from 'react'
import {Disclosure} from '@headlessui/react'
import {Bars3Icon, XMarkIcon} from '@heroicons/react/24/outline'
import axios from "axios";

const navigation = [
    {name: 'Lista użytkowników', href: '#', current: false},
    {name: 'Lista schronisk', href: '/shelterList', current: false}
]

function classNames(...classes) {
    return classes.filter(Boolean).join(' ')
}

export default function NavbarAdmin() {
    axios.defaults.withCredentials = true
    const navigate = useNavigate();

    const handleLogout = async () => {
        await axios.get("http://localhost:8080/auth/logout");
        localStorage.clear();
        navigate('/');
        await window.location.reload()
    };

    return (
        <Disclosure as="nav" className="bg-white">
            {({open}) => (
                <>
                    <div className="mx-auto max-w-6xl px-2 sm:px-4 lg:px-4">
                        <div className="relative flex h-16 items-center justify-between">
                            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                                {/* Mobile menu button*/}
                                <Disclosure.Button
                                    className="inline-flex items-center justify-center rounded-md p-2 text-brown  hover:text-orange focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                    <span className="sr-only">Open main menu</span>
                                    {open ? (
                                        <XMarkIcon className="block h-6 w-6" aria-hidden="true"/>
                                    ) : (
                                        <Bars3Icon className="block h-6 w-6" aria-hidden="true"/>
                                    )}
                                </Disclosure.Button>
                            </div>
                            <div className="flex flex-1 items-start justify-center sm:items-stretch sm:justify-start">
                                <Link to="/">
                                    <div className="flex w-48">
                                        <img
                                            className="blocklg:hidden"
                                            src={logo}
                                            alt="Logo schronisko.pl"
                                        />
                                        <img
                                            className="hidden lg:block"
                                            src={logo}
                                            alt="Logo schronisko.pl"
                                        />
                                    </div>
                                </Link>

                                <div className="hidden sm:ml-6 sm:block">
                                    <div className="flex space-x-4">
                                        {navigation.map((item) => (
                                            <a
                                                key={item.name}
                                                href={item.href}
                                                className={classNames(
                                                    item.current ? ' text-brown' : 'text-brown hover:text-orange',
                                                    'rounded-md px-3 py-2 text-sm font-medium active:text-bold'
                                                )}
                                                aria-current={item.current ? 'page' : undefined}
                                            >
                                                {item.name}
                                            </a>

                                        ))}
                                    </div>
                                </div>

                            </div>
                            <div
                                className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                                <button className="flex ml-4 hover:text-orange " onClick={handleLogout}>

                                    <div className="flex">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6"
                                             fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                                  stroke="#64290F"
                                                  d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                        </svg>
                                        <p className="text-brown hidden lg:block">Wyloguj się</p>
                                    </div>
                                </button>
                            </div>
                        </div>
                    </div>
                </>
            )}
        </Disclosure>
    )
}
