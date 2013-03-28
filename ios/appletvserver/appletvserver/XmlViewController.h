//
//  XmlViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Three20UI/Three20UI.h>
#import "MediaPlayer/MediaPlayer.h"
#import "MBProgressHUD.h"
@interface XmlViewController : TTViewController<UITableViewDelegate,TTImageViewDelegate,MBProgressHUDDelegate>{
    NSString* _xml;
    NSString* _script;
    NSString* _type;
    BOOL _append;
    NSMutableArray* _videos;
    TTStyledTextLabel* _summaryTextLabel;
    TTStyledTextLabel* _descriptionTextLabel;
    UIButton* _playButton;
    TTImageView* _imageView;
    UIView* _reflectImageView;
    UIScrollView* _scrollView;
    MPMoviePlayerViewController* _playerViewController;
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, copy) NSString* xml;
@property (nonatomic, copy) NSString* script;
@property (nonatomic, copy) NSString* type;
@property (nonatomic, assign) BOOL append;
@property (nonatomic, retain) NSMutableArray* videos;
@property (nonatomic, retain) TTStyledTextLabel* summaryTextLabel;
@property (nonatomic, retain) TTStyledTextLabel* descriptionTextLabel;
@property (nonatomic, retain) UIButton* playButton;
@property (nonatomic, retain) TTImageView* imageView;
@property (nonatomic, retain) UIView* reflectImageView;
@property (nonatomic, retain) UIScrollView* scrollView;
@property (nonatomic, retain) MPMoviePlayerViewController* playerViewController
;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
-(id) initWithXml:(NSString*) xml;
-(id) initWithScript:(NSString*) script;
-(void) appendXml:(NSString*) xml;
@end
