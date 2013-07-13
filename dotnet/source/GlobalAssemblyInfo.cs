using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;

// Global Descriptive Stuff
[assembly: AssemblyProduct("Common Messaging Framework")]
[assembly: AssemblyCompany("Berico Technologies, LLC")]
[assembly: AssemblyCopyright("Copyright ©  2013")]
[assembly: AssemblyTrademark("")]
[assembly: AssemblyCulture("")]

// Global Versioning
// When ready for release, remove the star.  For example, if you're releasing
// version 3.2, this value should be 3.2.0.0.  While you're working towards
// version 3.3, this value should be 3.2.0.*.  This way, snapshot versions
// will be higher than 3.2.0.0, and look like 3.2.0.1257, which is the 
// equivalent of 3.3.0-SNAPSHOT in java/maven terms
[assembly: AssemblyVersion("3.2.0.*")]
/* let MSBuild automatically update this:
 * [assembly: AssemblyFileVersion("3.0.0.0")]
 * see: 
 *   http://stackoverflow.com/questions/356543/can-i-automatically-increment-the-file-build-version-when-using-visual-studio
 */

// Global Configuration
#if DEBUG
[assembly: AssemblyConfiguration("Debug")]
#else
[assembly: AssemblyConfiguration("Release")]
#endif
