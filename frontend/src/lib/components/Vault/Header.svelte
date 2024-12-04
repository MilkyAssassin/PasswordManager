<script lang="ts">
    type sortType =
        | "AlphabeticalOrder"
        | "AlphabeticalOrderReverse"
        | "DateCreated"
        | "DateCreatedReverse";
    type props = {
        search: string;
        sort: sortType;
    };

    let { search = $bindable(), sort = $bindable() }: props = $props();
    let isShowProfile = $state(false);
    let sort_options: Array<sortType> = [
        "AlphabeticalOrder",
        "AlphabeticalOrderReverse",
        "DateCreated",
        "DateCreatedReverse",
    ];

    function translateSort(key: sortType) {
        switch (key) {
            case "AlphabeticalOrder":
                return "alphabetical order";

            case "AlphabeticalOrderReverse":
                return "reverse alphabetical order";

            case "DateCreated":
                return "date created - oldest first";

            case "DateCreatedReverse":
                return "date created - newest first";
        }
    }
</script>

<div class="w-full h-16 bg-white">
    <div class="h-4/6 relative top-1/2 -translate-y-1/2 ml-5 flex gap-4 pr-8">
        <div class="rounded-full bg-[#d8cbea] px-4 h-full w-96 flex">
            <span>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    class="h-full"
                    ><title>magnify</title><path
                        d="M9.5,4C13.09,4 16,6.91 16,10.5C16,12.12 15.41,13.6 14.43,14.73L20.08,20.38L19.37,21.09L13.72,15.44C12.59,16.41 11.11,17 9.5,17C5.91,17 3,14.09 3,10.5C3,6.91 5.91,4 9.5,4M9.5,5C6.46,5 4,7.46 4,10.5C4,13.54 6.46,16 9.5,16C12.54,16 15,13.54 15,10.5C15,7.46 12.54,5 9.5,5Z"
                    /></svg
                >
            </span>
            <input
                bind:value={search}
                type="text"
                name="search"
                placeholder="Search vault"
                id=""
                class="w-80 h-full bg-[#d8cbea]"
            />
        </div>
        <button
            onclick={() => {
                const index = Math.max(0, sort_options.indexOf(sort));
                const nextIndex = (index + 1) % sort_options.length;
                sort = sort_options[nextIndex];
            }}
        >
            <div class="flex h-3/4">
                <!-- https://pictogrammers.com/library/mdi/icon/eye-off-outline/ -->
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    class="h-full relative top-1/2 -translate-y-1/2"
                    ><title>sort</title><path
                        d="M18 21L14 17H17V7H14L18 3L22 7H19V17H22M2 19V17H12V19M2 13V11H9V13M2 7V5H6V7H2Z"
                    />
                </svg>
                <p class="whitespace-nowrap align-middle text-center leading-8">
                    Sort by {translateSort(sort)}
                </p>
            </div>
        </button>
        <div class="ml-auto">
            <div
                class="rounded-full bg-[#d8cbea] text-[#625282] h-7 w-7 justify-center items-center text-center align-middle leading-7
                overflow-hidden
                hover:overflow-visible
                relative
                "
            >
                <div>A</div>
                <div class="absolute left-1/2 -translate-x-1/2 p-5 bg-black">
                    <div
                        class="w-0 h-0 border-l-[50px] border-l-transparent border-b-[75px] border-b-yellow-500 border-r-[50px] border-r-transparent"
                    ></div>
                    <button>Logout</button>
                </div>
            </div>
        </div>
    </div>
</div>
