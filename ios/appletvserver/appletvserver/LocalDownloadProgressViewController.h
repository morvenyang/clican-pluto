//
//  LocalDownloadProgressViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import <UIKit/UIKit.h>

@interface LocalDownloadProgressViewController : TTTableViewController{
    NSTimer* _refreshTimer;
}
@property (nonatomic, retain) NSTimer* refreshTimer;
@end
