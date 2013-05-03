//
//  DownloadProgressViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-4-23.
//
//

#import <UIKit/UIKit.h>

@interface DownloadProgressViewController : UIViewController{
    NSTimer* _refreshTimer;
}
@property (nonatomic, retain) NSTimer* refreshTimer;
@end
