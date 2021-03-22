using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Yunqingad.RNYunqingad
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNYunqingadModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNYunqingadModule"/>.
        /// </summary>
        internal RNYunqingadModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNYunqingad";
            }
        }
    }
}
