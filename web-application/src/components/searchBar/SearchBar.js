import React, { useState } from 'react';
import './styles/searchbar.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function SearchBar({ searchQuery, handleSearchInputChange, handleSearchResults }) {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = () => {
        if (searchQuery.trim() !== '') {
            setIsLoading(true);

              axios.get(`http://localhost:5000/search?query=${encodeURIComponent(searchQuery)}`)
                .then(response => {
                  setIsLoading(false);
                  console.log(response.data);
                  handleSearchResults(response.data);
                  navigate('/searchResult');
                })
                .catch(error => {
                  setIsLoading(false);
                  console.error('An error occurred during search:', error);
                });
        }
    };

    return (
        <div>
            <div className="input-group">
                <div style={{ display: 'flex', justifyContent: 'row' }}>
                    <div className="form-outline">
                        <input
                            id="search-focus"
                            type="search"
                            className="form-control"
                            onChange={handleSearchInputChange}
                            placeholder="Enter your search query"
                            style={{
                                width: '25rem',
                                borderTopRightRadius: '0',
                                borderBottomRightRadius: '0',
                                borderTopLeftRadius: '2rem',
                                borderBottomLeftRadius: '2rem',
                                boxShadow: 'none',
                            }}
                            value={searchQuery}
                        />
                    </div>
                    <button
                        onClick={handleSubmit}
                        type="button"
                        className={`btn btn-primary ${isLoading ? 'loading-button' : ''
                            }`}
                        style={{
                            width: '5rem',
                            borderTopRightRadius: '2rem',
                            borderBottomRightRadius: '2rem',
                            borderTopLeftRadius: '0',
                            borderBottomLeftRadius: '0',
                        }}
                        disabled={isLoading}
                    >
                        <i className="fas fa-search"></i>
                    </button>
                </div>
            </div>
        </div>
    );
}

export default SearchBar;
