//
//  LocalDownloadProgressViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import <UIKit/UIKit.h>
#import "MediaPlayer/MediaPlayer.h"
@interface LocalDownloadProgressViewController : TTTableViewController{
    NSTimer* _refreshTimer;
    MPMoviePlayerViewController* _playerViewController;
}
@property (nonatomic, retain) NSTimer* refreshTimer;
@property (nonatomic, retain) MPMoviePlayerViewController* playerViewController;
@end
