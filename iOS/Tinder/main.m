#import <UIKit/UIKit.h>
#import "UIImage+Additions_568.h"

#import "TinderAppDelegate.h"

int main(int argc, char *argv[])
{
    @autoreleasepool
    {
         [UIImage patchImageNamedToSupport568Resources];
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([TinderAppDelegate class]));
    }
}
