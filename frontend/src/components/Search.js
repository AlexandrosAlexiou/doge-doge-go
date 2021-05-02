import React, { useState } from 'react'
import './Search.css'
import SearchIcon from "@material-ui/icons/Search"
import MicIcon from "@material-ui/icons/Mic";
import { Button } from "@material-ui/core";
import { useHistory } from 'react-router';
import { useStateValue } from "../StateProvider";
import { actionTypes } from "../reducer";

function Search( { hideButtons = false } ) {
    const [, dispatch] = useStateValue();

    // Catch the input from search bar
    const [input, setInput] = useState("");
    const history = useHistory();

    const search = (e) => {
        e.preventDefault();

        dispatch({
            type: actionTypes.SET_SEARCH_TERM,
            term: input,
        });

        history.push('/search');
    };

    const lucky = (e) => {
        e.preventDefault();
        history.push('/lucky');
    };

    return (
        <form className='search'>
            <div className='search__input'>
                <SearchIcon className='search__inputIcon' />
                <input value={input} onChange={e => setInput(e.target.value)} />
                <MicIcon />
            </div>

            {!hideButtons ? (
                 <div className='search__buttons'>
                    <Button type='submit' onClick={search} variant='outlined'>Search</Button>
                    <Button type='submit' onClick={lucky} variant='outlined'>I'm Feeling Doge</Button>
                 </div>
            ): (
                <div className='search__buttons'>
                    <Button className='search__buttonsHidden' 
                    type='submit' onClick={search} variant='outlined'>
                        Search
                    </Button>
                </div>
            )}
        </form>
    )
}

export default Search