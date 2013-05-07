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
    NSMutableArray* _formats;
    NSString* _format;
    int _navigationIndex;
    TTStyledTextLabel* _formatTextLabel;
    NSString* _navigationScript;
    int _lastLines;
    TTTableView* _tableView;
    UIView* _tableBannerView;
    
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
@property (nonatomic, copy) NSString* format;
@property (nonatomic, retain) NSMutableArray* formats;
@property (nonatomic, retain) TTStyledTextLabel* formatTextLabel;
@property (nonatomic, copy) NSString* navigationScript;
@property (nonatomic, retain) TTTableView* tableView;
@property (nonatomic, retain) UIView* tableBannerView;
-(id) initWithXml:(NSString*) xml;
-(id) initWithScript:(NSString*) script;
-(void) appendXml:(NSString*) xml;
@end
