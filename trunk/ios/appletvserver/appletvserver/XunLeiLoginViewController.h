//
//  XunLeiLoginViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import <UIKit/UIKit.h>

@interface XunLeiLoginViewController : UIViewController{
    UIWebView* _webView;
    int _type;
}
@property (nonatomic, retain) UIWebView* webView;
-(id)initWithType:(int) type;
@end
