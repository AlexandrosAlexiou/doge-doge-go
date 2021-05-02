import React from 'react'
import './Home.css'
import Search from '../components/Search';

function Home() {
    return (
        <div className='home'>
            <div className='home__header'>
                <div className='home__headerLeft'>
                    <a href='https://github.com/AlexandrosAlexiou' rel="noopener noreferrer" target="_blank">Alexiou</a>
                    <a href='https://github.com/giannisChouliaras' rel="noopener noreferrer" target="_blank">Chouliaras</a>
                </div>
                <div className='home__headerRight'>
                    <a href='http://cs.uoi.gr/~pitoura/' rel="noopener noreferrer" target="_blank">Professor</a>
                    <a href='http://cs.uoi.gr/~pitoura/courses/ap/ap21/index.html' rel="noopener noreferrer" target="_blank">Course</a>
                </div>
            </div>
            
            
            <div className='home__body'>
                <div className='svg_wrapper'>
                    <svg width="200" height="200" viewBox="-5 0 50 40"
                                fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path
                        d="M10.0006 0H29.3993C34.9418 0 39.3999 4.52599 39.3999 10.1529V29.8471C39.3999 35.474 34.9418 40 29.3993 40H10.0006C4.45809 40 0 35.474 0 29.8471V10.1529C0 4.52599 4.45809 0 10.0006 0Z"
                        fill="#EFE7DC"></path>

                    <path
                        d="M5.30238 22.1408C5.18189 21.2846 5.66385 20.4283 6.38678 20.0613C7.95314 19.0827 9.88096 18.9604 11.5678 19.6944C12.1703 20.0613 12.7727 20.306 13.3751 20.7953C13.9776 21.1622 14.2186 22.0185 13.8571 22.7525C13.3751 23.6087 12.8932 24.3427 12.1703 25.0766C10.8449 26.7892 8.4351 27.0338 6.62776 25.6882C6.50727 25.5659 6.26629 25.4436 6.14581 25.1989C5.54336 24.3427 5.18189 23.2418 5.30238 22.1408Z"
                        fill="black"></path>

                    <path 
                        d="M22.6523 31.315C20.6039 31.9266 18.5556 33.0275 16.2663 32.5383C15.9049 32.4159 15.5434 32.2936 15.1819 32.1713C13.4951 31.4373 11.6878 30.948 9.88043 30.8257C8.43456 30.5811 7.10919 30.0918 5.9043 29.3578C6.14527 29.2355 6.38625 29.2355 6.62723 29.2355C7.83212 28.9909 9.1575 29.2355 10.3624 29.7248C11.8083 30.4587 13.2541 30.8257 14.7 31.682C15.6639 32.049 16.7483 32.1713 17.7122 31.8043C18.7966 31.5597 19.7605 31.315 20.8449 31.0704C21.4474 31.0704 22.0498 31.0704 22.6523 31.0704V31.315Z"
                        fill="black"></path>

                    <path
                         d="M13.2528 10.8868C12.8914 9.78589 11.566 9.17427 10.4816 9.66356C10.3611 9.66356 10.3611 9.66356 10.2406 9.78589C9.27671 10.1529 8.79476 11.0091 8.67427 11.9877C8.67427 12.11 8.67427 12.3547 8.55378 12.477C8.3128 13.5779 8.67427 13.9449 9.75867 14.0672C10.1201 14.0672 10.4816 14.0672 10.8431 14.0672C11.9275 14.0672 12.8914 13.3333 13.2528 12.2324C13.3733 11.8654 13.3733 11.3761 13.2528 10.8868ZM10.3611 13.8226C9.63818 13.7003 9.15622 13.0886 9.15622 12.477C9.27671 11.2538 10.1201 10.2752 11.325 10.1529C9.3972 11.0091 9.63818 12.3547 10.3611 13.8226Z"
                         fill="black"></path>

                    <path
                        d="M25.6633 17.6147C24.9404 17.4924 24.3379 17.4924 23.7355 17.2477C23.133 17.0031 22.8921 16.3915 23.0125 15.6575C23.374 13.945 24.8199 12.7217 26.5067 12.3548C28.3141 11.9878 30.1214 12.9664 30.9648 14.6789C31.0853 14.9236 30.8443 15.4129 30.8443 15.7798H30.6033V14.8012L29.3985 16.2691C28.6755 17.0031 27.7116 17.3701 26.6272 17.2477C26.1453 17.2477 26.0248 16.8808 26.0248 16.3915C26.0248 15.7798 26.0248 15.2905 26.1453 14.6789C26.2657 14.1896 26.5067 13.578 26.7477 13.0887C26.0248 12.7217 25.0609 13.3334 24.3379 14.5566C23.856 15.5352 24.2174 16.3915 25.6633 17.6147Z"
                        fill="black"></path>
                    </svg>

                    <h1>DogeDogeGo</h1>
                </div>
                <div className='home__inputContainer'>
                    <Search />
                </div>

            </div>
        </div>
    )
}

export default Home