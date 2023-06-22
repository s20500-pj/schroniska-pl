import logo from "../LogoSchroniska-pl.png";
import {Link} from "react-router-dom";
import React, {Fragment} from 'react'
import {Disclosure, Menu, Transition} from '@headlessui/react'
import {Bars3Icon, XMarkIcon} from '@heroicons/react/24/outline'

const navigation = [
    {name: 'LISTA SCHRONISK', href: '/shelterList', current: false},
    {name: 'LISTA ZWIERZĄT', href: '/animalList', current: false},
    {name: 'KONTAKT', href: '/contact', current: false}

]

function classNames(...classes) {
    return classes.filter(Boolean).join(' ')
}

export default function Nav() {
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
                                            className="block lg:hidden"
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
                                    <div className="flex m-4">
                                        {navigation.map((item) => (
                                            <a
                                                key={item.name}
                                                href={item.href}
                                                className={classNames(
                                                    item.current ? ' text-brown text-center ' : 'text-brown hover:text-orange hover:underline-offset-1',
                                                    'm-2 text-sm'
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


                                {/* Profile dropdown */}
                                <Menu as="div" className="relative ml-3 hidden md:block">
                                    <div>
                                        <Menu.Button
                                            className="flex rounded-2xl p-1 bg-orange text-sm focus:ring-1 focus:ring-brown focus:ring-offset-1 sm:rounded sm:p-1">
                                            <a className="text-sm pl-1"
                                               href="src/layout">Zarejestruj</a>
                                            <svg
                                                className="w-5 h-5 ml-1" aria-hidden="true" fill="currentColor"
                                                viewBox="0 0 20 20"
                                                xmlns="http://www.w3.org/2000/svg">
                                                <path fillRule="evenodd"
                                                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                                      clipRule="evenodd"></path>
                                            </svg>
                                        </Menu.Button>
                                    </div>
                                    <Transition
                                        as={Fragment}
                                        enter="transition ease-out duration-100"
                                        enterFrom="transform opacity-0 scale-95"
                                        enterTo="transform opacity-100 scale-100"
                                        leave="transition ease-in duration-75"
                                        leaveFrom="transform opacity-100 scale-100"
                                        leaveTo="transform opacity-0 scale-95"
                                    >
                                        <Menu.Items
                                            className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none">
                                            <Menu.Item>
                                                {({active}) => (
                                                    <Link to="/adduser">
                                                        <a
                                                            href="src/layout#"
                                                            className={classNames(active ? 'bg-gray-200' : '', 'block px-4 py-2 text-sm text-brown')}
                                                        >
                                                            użytkownika
                                                        </a>
                                                    </Link>
                                                )}
                                            </Menu.Item>
                                            <Menu.Item>
                                                {({active}) => (
                                                    <Link to="/addshelter">
                                                        <a
                                                            href="src/layout#"
                                                            className={classNames(active ? 'bg-gray-200' : '', 'block px-4 py-2 text-sm text-brown')}
                                                        >
                                                            schronisko
                                                        </a>
                                                    </Link>
                                                )}
                                            </Menu.Item>

                                        </Menu.Items>
                                    </Transition>
                                </Menu>
                                <button className="flex ml-4 hover:text-orange ">
                                    <Link to="/login">
                                        <div className="flex">
                                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6"
                                                 fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                                      stroke="#64290F"
                                                      d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                                            </svg>
                                            <p className="text-brown hidden lg:block">Zaloguj się</p>
                                        </div>
                                    </Link>
                                </button>
                            </div>
                        </div>
                    </div>

                    <Disclosure.Panel className="sm:hidden">
                        <div className="space-y-1 px-2 pt-2 pb-3">
                            {navigation.map((item) => (
                                <Disclosure.Button
                                    key={item.name}
                                    as="a"
                                    href={item.href}
                                    className={classNames(
                                        item.current ? 'bg-orange text-brown' : 'text-orange hover:bg-orange hover:text-brown',
                                        'block rounded-md px-3 py-2 text-base font-medium'
                                    )}
                                    aria-current={item.current ? 'page' : undefined}
                                >
                                    {item.name}

                                </Disclosure.Button>

                            ))}
                            <div className="space-y-1 px-2 pt-2 pb-3">
                                <Disclosure.Button>
                                    <a
                                        href="/adduser"
                                        className='text-orange hover:bg-orange hover:text-brown block rounded-md px-3 py-2 text-base font-medium'
                                    >
                                        Rejestracja użytkownika
                                    </a>

                                </Disclosure.Button>
                            </div>
                            <div className="space-y-1 px-2 pt-2 pb-3">
                                <Disclosure.Button>
                                    <a
                                        href="/addshelter"
                                        className='text-orange hover:bg-orange hover:text-brown block rounded-md px-3 py-2 text-base font-medium'
                                    >
                                        Rejestracja schroniska
                                    </a>

                                </Disclosure.Button>
                            </div>

                        </div>
                    </Disclosure.Panel>
                </>
            )}
        </Disclosure>
    )
}
