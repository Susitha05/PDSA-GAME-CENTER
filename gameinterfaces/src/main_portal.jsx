import React from 'react'
import LOGO from "./assets/img/LOGO.png"

function mainPortal(){
    return(
        <div className="flex w-full h-screen bg-green-950 items-center">
            <div className="flex w-full h-full justify-center items-center">
                <img className="w-72 h-72 object-fill" src={LOGO} alt="LOGO"/>
            </div>
        </div>
    )
}
export default mainPortal