Cache Memory Simulator

The Cache Memory Simulator is a desktop application developed in Java using the JavaFX framework, designed to model and visualize the behavior of a cache memory system in a clear and interactive manner. The application allows users to explore how different cache mapping strategies operate and how memory access patterns influence cache performance.

Upon launching the application, the user is presented with a configuration interface where the cache simulation parameters can be defined. The user may select the cache mapping strategy and specify the cache size. The application validates the provided input to ensure that the cache size is a power of two, preventing invalid configurations and ensuring consistent behavior during simulation.

Once the simulation is started, the main interface displays the current state of the cache memory. Memory addresses can be entered manually, and each address access is processed immediately by the simulator. For every access, the application determines whether the requested address results in a cache hit or a cache miss, based on the selected cache architecture. The accessed cache line is visually highlighted, allowing the user to directly observe how the cache content changes over time.

The cache is represented in a tabular format that includes the cache line index, the valid bit, the tag, and the stored data. This representation enables users to inspect the internal state of the cache at any moment and to understand how addresses are mapped to cache lines under different configurations. In the case of a fully associative cache, the FIFO replacement policy is applied when the cache is full, and eviction events are explicitly recorded.

Throughout the simulation, the application continuously updates detailed performance statistics. These statistics include the total number of memory accesses, the number of cache hits, the number of cache misses, the hit rate expressed as a percentage, and the total elapsed time of the simulation. Additionally, eviction events are logged and displayed, providing insight into which cache lines are replaced, the corresponding tags, and the accessed addresses that triggered the replacement.

The user may reset the simulation at any time, which clears the cache content, resets all revealed statistics, and restarts the timing mechanism while preserving the initial configuration parameters. A simulation summary can also be generated at the end of execution, presenting an aggregated view of all collected performance metrics.

From an architectural standpoint, the application is built using a modular and maintainable design. The simulation logic is fully decoupled from the graphical user interface, ensuring clean separation between computation and presentation layers. This design allows the simulator to be easily extended with additional cache configurations, alternative replacement policies, or more advanced visualization features without major structural changes.

The graphical user interface is implemented using FXML layouts and styled through a custom CSS theme inspired by flat user interface principles. The interface prioritizes readability, responsiveness, and user experience, making the application suitable for demonstrations, experimentation, and technical evaluations.

This project demonstrates strong proficiency in cache memory concepts, performance analysis, object-oriented software design, and JavaFX-based graphical application development. It is intended to serve as a practical tool for experimentation as well as a representative example of applied systems programming and user interface engineering.

Author
Iacob Bianca
